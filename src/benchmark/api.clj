(ns benchmark.api
  (:require [criterium.core :as crit]))


(defmacro benchmark-us
  "Benchmark an op, returning a map of :mean and :variance in μs."
  [op]
  `(let [bdata# (c/quick-benchmark ~op nil)]
     {:mean-μs (* (double (first (:mean bdata#))) 1e6)
      :variance-μs (* (double (first (:variance bdata#))) 1e6)}))
