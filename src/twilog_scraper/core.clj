(ns twilog-scraper.core
  (:require [net.cgrand.enlive-html :as html]
            [skyscraper :as s]
            [twilog-scraper.middleware :as m]))

(defn seed [username]
  (let [url (str "http://twilog.org/" username)]
    [{:username username
      :url url
      :processor ::user-page}]))

(s/defprocessor user-page
  :cache-template "twilog/:username"
  :process-fn (fn [res {:keys [username]}]
                (let [not-registered (seq (html/select res [:div.box-info.box-icon]))
                      not-found (seq (html/select res [:div.box-attention.box-icon]))]
                  (cond
                    not-registered [{:msg "This account was not registered."}]
                    not-found [{:msg "This account was not found."}]
                    :else [{:url (str "http://twilog.org/" username "/archives")
                            :processor ::archives-page}]))))

(s/defprocessor archives-page
  :cache-template "twilog/:username/archives"
  :process-fn (fn [res context]
                (let [month-links (html/select res [[:div#content] [:a.side-list-icon (html/attr= :title "日別ツイート一覧")]])]
                  (for [link month-links
                        :let [url (s/href link)
                              month (last (re-find #"monthlist-(:?\d+)" url))]]
                    {:month month
                     :url url
                     :processor ::monthlist-page}))))

(s/defprocessor monthlist-page
  :cache-template "twilog/:username/months/:month"
  :process-fn (fn [res context]
                (let [day-links (html/select res [:div#content :ul.side-list.wide :a])]
                  (for [link day-links
                        :let [url (s/href link)
                              day (last (re-find #"date-(:?\d+)" url))]]
                    {:day day
                     :url url
                     :processor ::day-page}))))

(defn- tweet-attr [t selector]
  (html/text (first (html/select t selector))))

(s/defprocessor day-page
  :cache-template "twilog/:username/days/:day"
  :process-fn (fn [res context]
                (let [tweets (html/select res [:article.tl-tweet])]
                  (for [t tweets
                        :let [name (tweet-attr t [:p.tl-name :a :span])
                              screen-name (tweet-attr t [:p.tl-name :a :strong])
                              text (tweet-attr t [:p.tl-text])
                              posted-at (tweet-attr t [:p.tl-posted :a])
                              retweeted? (seq? (seq (tweet-attr t [:p.tl-retweet])))]]
                    {:tweet-content
                     {:name name
                      :screen-name screen-name
                      :text text
                      :posted-at posted-at
                      :retweeted retweeted?}}))))

(defn- wrap [handler middleware opts]
  (if (true? opts)
    (middleware handler)
    (if opts
      (middleware handler opts)
      handler)))

(defn create-handler
  "Options:
     :max
     :from
     :until"
  [handler options]
  (-> handler
      m/wrap-format
      m/wrap-only-tweet
      (wrap m/wrap-take (:max options))
      (wrap m/wrap-from (:from options))
      (wrap m/wrap-until (:until options))
      m/wrap-datetime
      m/wrap-ignore-secret-tweet))

(defn scrape [username {:as options
                        :keys [html-cache processed-cache]
                        :or {html-cache true processed-cache true}}]
  (let [handler (create-handler identity options)]
    (handler (s/scrape (seed username) :html-cache html-cache :processed-cache processed-cache))))
