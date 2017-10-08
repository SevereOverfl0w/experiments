(ns myproject.collect-results
  (:require
    [clojure.test :as t]
    [myproject.test.sample :as s]))

(defmulti capturing-report :type)

(def ^:dynamic *results-capture*)

(defmethod capturing-report :default [m])

(defn add-result
  [extraction formatter]
  (let [[code result] extraction]
    (when *results-capture*
      (dosync (commute *results-capture*
                       (fn [m]
                         (if (:current-var m)
                           (update m (:current-var m) (fnil conj [])
                                   (formatter code result))
                           m)))))))

(defmethod capturing-report :pass [m]
  (add-result [(nth (:expected m) 2) (nth (:actual m) 1)]
              #(format "=> %s\n%s" %1 %2)))

(defmethod capturing-report :fail [m]
  (add-result [(nth (:expected m) 2) (-> m :actual second second)]
              #(format "=> %s ;; Doesn't produce:\n%s" %1 %2)))

(defmethod capturing-report :error [m])

(defmethod capturing-report :summary [m])
;; Ignore these message types:
(defmethod capturing-report :begin-test-ns [m])
(defmethod capturing-report :end-test-ns [m])
(defmethod capturing-report :begin-test-var [m]
  (when (and *results-capture*
             (:document-me (meta (:var m))))
    (dosync (commute *results-capture* assoc :current-var (:var m)))))
(defmethod capturing-report :end-test-var [m]
  (when *results-capture*
    (dosync (commute *results-capture* dissoc :current-var))))

(defn collect-results
  []
  (let [report* t/report]
    (binding [*results-capture* (ref {})
              t/report capturing-report]
      (t/run-all-tests #"myproject\..*")
      @*results-capture*)))
