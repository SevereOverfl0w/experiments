(ns myproject.test.sample
  (:require [clojure.test :as t]))

(t/deftest ^:document-me
  some-test
  (t/is (= 4 (+ 2 3)))
  (t/is (= 4 (+ 2 2))))

(t/deftest some-other-test
  (t/is (= 4 (+ 2 3))))
