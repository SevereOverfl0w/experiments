(ns myproject.main
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [myproject.collect-results :as collect-results])
  (:import
    [org.asciidoctor Asciidoctor$Factory]
    [org.asciidoctor.extension IncludeProcessor]))

(defn format-result
  [repl-seq]
  (string/join "\n\n" repl-seq))

(defn register-test-include-processor [engine results]
  (.includeProcessor
    (.javaExtensionRegistry engine)
    (proxy [IncludeProcessor] []
      (handles [target]
        (string/starts-with? target "cljtest://"))
      (process [document reader target attributes]
        (let [important-target (string/replace-first target "cljtest://" "#'")]
          (doto reader
            (.push_include
              (format-result
                (second
                  (first
                    (filter
                      (fn [[k _]]
                        (= (str k) important-target))
                      results))))
              target
              target
              1
              attributes)))))))

(defn engine []
  (Asciidoctor$Factory/create))

(defn render
  [engine file opts]
  (.convertFile engine (io/file file) opts))

(defn main
  [files output-dir]
  (let [engine (doto (engine)
                 (register-test-include-processor (collect-results/collect-results)))]
    (run! #(render engine % {"to_dir" output-dir
                             "safe" (.getLevel org.asciidoctor.SafeMode/UNSAFE)})
          files)))
