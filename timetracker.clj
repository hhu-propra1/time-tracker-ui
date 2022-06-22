(ns timetracker
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:import [java.text SimpleDateFormat]))

(def csv-file
  (io/file (System/getProperty "user.home")
           ".config" "time-tracker" "tasks.csv"))

(def csv
  (with-open [reader (io/reader csv-file)]
    (doall (csv/read-csv reader))))

(defn convert-row
  "Konvertiert eine Zeile aus der CSV liest und sie in eine
  Clojure-Datenstruktur umwandelt."
  [row]
  (let [date-format (SimpleDateFormat. "yyyy-MM-dd")]
    {:datum (.parse date-format (first row))
     :minuten (Integer/parseInt (second row))
     :kurs (nth row 2)
     :beschreibung (nth row 3)}))

(def rows
  (map convert-row (rest csv)))

(defn sum-minutes [rows]
  (reduce + (map :minuten rows)))
