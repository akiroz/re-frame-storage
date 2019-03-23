(defproject akiroz.re-frame/storage "0.1.4"
  :description "re-frame interceptors for browser local storage"
  :url "https://github.com/akiroz/re-frame-storage"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}

  :dependencies [[alandipert/storage-atom "2.0.1"]]

  :profiles {:test {:plugins [[lein-cljsbuild "1.1.7"]
                              [lein-doo "0.1.11"]
                              ]
                    :dependencies [[org.clojure/clojure "1.10.0"]
                                   [org.clojure/clojurescript "1.10.520"]
                                   [org.clojure/core.async "0.4.490"]
                                   [re-frame "0.10.6"]
                                   [reagent "0.8.1"]
                                   ]
                    :doo {:paths {:karma "./node_modules/karma/bin/karma"}}
                    :cljsbuild {:builds [{:id "test"
                                          :source-paths ["src" "test"]
                                          :compiler {:output-dir "target/js/out"
                                                     :output-to "target/js/testable.js"
                                                     :main akiroz.re-frame.storage-runner
                                                     :optimizations :whitespace
                                                     :npm-deps {:karma "4.0.1"
                                                                :karma-cljs-test "0.1.0"
                                                                :karma-firefox-launcher "1.1.0"}
                                                     :install-deps true
                                                     }}]}}}

  )
