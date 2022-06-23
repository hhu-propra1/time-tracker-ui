(ns site
  (:require [clojure.string :as str]
            [hiccup.util :refer [raw-string]]
            [hiccup2.core :as html]
            [timetracker :as tt]))

(defn base [& content]
  [:html {:lang "de"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:link {:href "node_modules/bootstrap/dist/css/bootstrap.min.css"
            :rel "stylesheet"}]
    [:script {:src "node_modules/chart.js/dist/chart.min.js"}]
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

(def kurs-zu-minuten
  "Vorarbeit, um die summierten Minuten pro Kurs zu erhalten."
  (->> (tt/rows)
       (group-by :kurs)
       (map (fn [[kurs entries]]
              {:kurs kurs
               :minuten (tt/sum-minutes entries)}))))

(def kurse
  "Alle eindeutigen Kurse."
  (->> (map :kurs kurs-zu-minuten)
       (map #(format "\"%s\"" %))
       (str/join ",")))

(def minuten
  "Verbrachte Minuten von den Kursen."
  (->> (map :minuten kurs-zu-minuten)
       (str/join ",")))

(def chart
  [:section
   [:canvas#chart]
   [:script
    (raw-string
     (format
      "
      const data = {
        labels: [%s],
        datasets: [{
          label: 'Timetracker',
          data: [%s],
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)'
          ],
        }]
      }
      const config = {
        type: 'bar',
        data: data,
        options: {}                  
      }
      const myChart = new Chart(
        document.getElementById('chart'),
        config
      );" kurse minuten))]])

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
     (map row->tr (tt/rows))]]
   [:h2 "Projektzeitverteilung"]
   chart))

;; -----------------------------------------------------------------------------

(spit "index.html" (html/html (index-page)))
