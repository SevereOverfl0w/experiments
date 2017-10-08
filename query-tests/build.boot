(set-env!
  :dependencies '[[org.asciidoctor/asciidoctorj "1.6.0-alpha.5"]]
  :source-paths #{"src"})

(require '[myproject.main :refer [main]])

(deftask go
  []
  (let [output-dir (doto (tmp-dir!) (.mkdirs))]
    (with-pre-wrap fs
      (main (map
              tmp-file
              (by-ext [".adoc" ".ad" ".asciidoctor"]
                      (input-files fs)))
            (.getAbsolutePath output-dir))
      (-> fs (add-resource output-dir) (commit!)))))
