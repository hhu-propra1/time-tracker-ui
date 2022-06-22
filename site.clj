(ns site
  (:require [hiccup2.core :as html]
            [timetracker :as tt]))

;; C-c C-c zum S-Expression evaluieren

(defn base [& content]
  [:html {:lang "de"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:link {:href "node_modules/bootstrap/dist/css/bootstrap.min.css"
            :rel "stylesheet"}]
    [:title "Titel"]]
   [:body.container.pt-3
    [:div#main
     content]]])

;; -----------------------------------------------------------------------------

(defn row->tr
  "Konvertiere eine Zeile aus der CSV in eine HTML Tabellenzeile."
  [{:keys [datum minuten kurs beschreibung]}]
  [:tr
   [:td datum]
   [:td minuten]
   [:td kurs]
   [:td beschreibung]])

(defn index-page []
  (base
   [:h1 "#TimeTracker"]
   [:table.table.table-striped.table-hover
    [:thead
     [:tr
      [:th "Datum"]
      [:th "Minuten"]
      [:th "Kurs"]
      [:th "Beschreibung"]]]
    [:tbody
     (map row->tr tt/rows)]]))

;; -----------------------------------------------------------------------------

(spit "index.html" (html/html (index-page)))
