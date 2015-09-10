(ns twilog-scraper.main
  (:require [cling.core :refer [defcmd defentrypoint] :as cli]
            [skyscraper :as s]
            [twilog-scraper.core :as ts])
  (:gen-class))

;; TODO: logger?
(alter-var-root #'s/log (constantly (fn log [& args])))

(defn- parse-int [s]
  (Integer/valueOf s 10))

(defn- parse-bool [s]
  (Boolean/valueOf s))

(defcmd scrape
  [["-m" "--max max" "Max number of Tweets"
    :default 200 :parse-fn parse-int :validate-fn [pos?] :validate-msg ["Must be a positive"]]
   ["-U" "--unlimited" "Ignore max number of Tweets"
    :default false :parse-fn parse-bool]
   ["-f" "--from from" "From when (8 digits format, like 20150101)"]
   ["-u" "--until until" "Until when (8 digits format, like 20151201)"]
   ["-H" "--html-cache bool" "Use HTML-cache"
    :default true :parse-fn parse-bool]
   ["-P" "--processed-cache bool" "Use processed cache"
    :default true :parse-fn parse-bool]]
  [["username"]]
  [{:keys [options arguments] :as env}]
  (let [username (:username arguments)
        unlimited (:unlimited options)
        options (if unlimited (dissoc options :max) options)]
    (try
      (-> (doall (ts/scrape username options))
          cli/ok)
      (catch Exception e#
        (cli/fail! (.getMessage e#))))))

(defentrypoint -main
  scrape)
