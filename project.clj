(defproject synthetic-event-test "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2234"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.6.4"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "synthetic-event-test"
              :source-paths ["src"]
              :compiler {
                :output-to "synthetic_event_test.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
