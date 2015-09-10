(defproject ayato_p/twilog-scraper "0.1.1"
  :description "Scraping library/cli tool just for Twilog"
  :url "https://github.com/ayato-p/twilog-scraper"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [skyscraper "0.1.1"]
                 [rkworks/cling "0.1.2-SNAPSHOT"]
                 [clj-time "0.11.0"]]
  :profiles {:dev {:source-paths ["env/exec"]
                   :main twilog-scraper.main}
             :uberjar {:uberjar-name "twilog-scraper.jar"
                       :aot :all
                       :source-paths ["env/exec"]
                       :main twilog-scraper.main}})
