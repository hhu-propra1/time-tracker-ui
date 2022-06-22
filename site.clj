(ns site
  (:require [hiccup2.core :as html]))

;; C-c C-c zum S-Expression evaluieren

(defn base [& content]
  [:html {:lang "de"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title "Titel"]]
   [:body
    [:div#main
     content]]])

;; -----------------------------------------------------------------------------

(defn index-page []
  (base
   [:h1 "Hallo!"]
   [:p "Dies ist irgendein Text"]
   [:img {:src "https://www.hhu.de/typo3conf/ext/wiminno/Resources/Public/img/hhu_logo_mobil.png"}]))

;; -----------------------------------------------------------------------------

(spit "index.html" (html/html (index-page)))
