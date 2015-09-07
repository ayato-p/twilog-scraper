(ns twilog-scraper.middleware
  (:require [clj-time.coerce :as tc]
            [clj-time.core :as t]
            [clj-time.format :as tf]))

(defn wrap-take [handler max]
  (fn [res]
    (handler (take max res))))

(def ^:private from+until-format
  (tf/formatter "yyyyMMdd"))

(defn wrap-from [handler from]
  (let [from (tc/to-date (tf/parse-local from+until-format from))]
    (fn [res]
      (let [c #(pos? (compare (get-in % [:tweet-content :posted-at])
                              from))]
        (handler (take-while c res))))))

(defn wrap-until [handler until]
  (let [until (tc/to-date (t/plus (tf/parse-local from+until-format until) (t/days 1)))]
    (fn [res]
      (let [c #(pos? (compare (get-in % [:tweet-content :posted-at])
                              until))]
        (handler (drop-while c res))))))

(def ^:private posted-at-format
  (tf/formatter "yyyyMMdd HH:mm:ss"))

(defn- convert-posted-at [m]
  {:pre [(contains? m :day)
         (contains? (:tweet-content m) :posted-at)]}
  (let [day (:day m)
        posted-at (get-in m [:tweet-content :posted-at])]
    (assoc-in m [:tweet-content :posted-at]
              (->>  (str "20" day " " posted-at) ;; oops!!
                    (tf/parse-local posted-at-format)
                    (tc/to-date)))))

(defn wrap-datetime [handler]
  (fn [res]
    (handler (pmap convert-posted-at res))))

(defn wrap-only-tweet [handler]
  (fn [res]
    (handler (pmap :tweet-content res))))

(defn wrap-ignore-secret-tweet [handler]
  (fn [res]
    (handler (filter #(seq (get-in % [:tweet-content :posted-at])) res))))

(defn wrap-format [handler]
  (fn [res] (handler res)))
