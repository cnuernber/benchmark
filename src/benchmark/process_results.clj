(ns benchmark.process-results
  (:require [tech.v3.dataset :as ds]
            [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import [java.io File]))


(defn list-results
  [dirname]
  (->> (File. (str dirname "/"))
       (.list)
       (remove #(= % ".keepme"))
       (map #(str dirname "/" %))))


(defn round-columns
  [ds cnames ndigits]
  (let [ndigits (double (Math/pow 10.0 ndigits))]
    (reduce (fn [ds cname]
              (ds/column-map ds cname (fn [val]
                                        (when val
                                          (-> (* (double val) ndigits)
                                              (Math/round)
                                              (/ ndigits))))
                             [cname]))
            ds
            cnames)))


(defn result-file->dataset
  [fname column-order numeric-columns]
  (let [data (->> (slurp fname)
                  (edn/read-string))
        ds (-> data
               :dataset
               (ds/->dataset)
               (ds/select-columns column-order)
               (round-columns numeric-columns 3)
               ;;Always print entire dataset
               (ds/head 10000))]
    (vary-meta ds merge (-> (dissoc data :dataset)
                            (assoc :name fname)))))
