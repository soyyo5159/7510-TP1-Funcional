(ns main (:require 
  [tratar-input :refer :all]
  [Premisa :as p]
  [BaseDeDatos :refer :all]
))

(defn- base-trucha [pregunta]
  (case pregunta
      "llueve?" "si"
      "esta mojado?" "si"
      "hay sol?" "no"
      "playa?" "no"))

(defn leer-usuario [] (read-line))
(defn mostrar-usuario [x] (println x))
(defn- preguntar-strings [base entrada]
  (let [
    pregunta (p/parsear entrada)
    respuesta (verifica? base pregunta)
    ]
    (cond
      (nil? pregunta) "No se entiende la pregunta"
      respuesta "si"
      (not respuesta) "no"
    )
  )
)
(defn -main 
  "holi"
  [nombre-archivo]
  (let [
    base (parsearBaseDeDatos (slurp nombre-archivo))
    ]
    (if (nil? base)
      (mostrar-usuario "La base de datos no puede ser leida")
      (tratar-input leer-usuario mostrar-usuario (partial preguntar-strings base))
    )
    
  )
)