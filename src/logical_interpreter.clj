(ns logical-interpreter 
  (:require 
    [BaseDeDatos :refer :all]
    [parsear :refer :all]
    [Error :refer :all]
  )
)

(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil"
  [database query]
  (let [
    p-db (parsear database)
    p-q (parsear query)
    res (cond
      (error? p-db) nil
      (error? p-q) nil
      :else (fmap verifica? p-db p-q)
    )
  ]
    (if (error? res) nil res)
    ; el error tiene un mensaje de error, pero debo sacarselo :(
  )
)
