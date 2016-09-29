(defproject akiroz.re-frame/storage "0.1.0"
  :description "re-frame interceptors for browser local storage"
  :url "https://github.com/akiroz/re-frame-storage"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}

  :dependencies [[alandipert/storage-atom "2.0.1"]]

  :profiles {:test {:plugins [[lein-cljsbuild "1.1.4"]
                              [lein-doo "0.1.7"]]
                    :dependencies [[org.clojure/clojure "1.8.0"]
                                   [org.clojure/clojurescript "1.9.229"]
                                   [org.clojure/core.async "0.2.391"]
                                   [re-frame "0.8.0"]
                                   [reagent "0.6.0"]
                                   ]
                    :cljsbuild {:builds [{:id "test"
                                          :source-paths ["src" "test"]
                                          :compiler {:output-dir "target/js/out"
                                                     :output-to "target/js/testable.js"
                                                     :main akiroz.re-frame.storage-runner
                                                     :optimizations :whitespace}}]}}}

  )
