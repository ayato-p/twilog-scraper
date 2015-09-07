(defproject twilog-scraper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [skyscraper "0.1.1"]
                 [rkworks/cling "0.1.2-SNAPSHOT"]
                 [clj-time "0.11.0"]]
  :uberjar-name "twilog-scraper.jar"
  :main twilog-scraper.main
  :profiles {:uberjar {:aot :all}})
