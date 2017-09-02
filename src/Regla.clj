(ns Regla)


(defprotocol Regla
    (nombre [this])
    (argumentos [this])
    (aridad [this])
    (verifica? [this premisa preguntar]))

(def implementacion-mala {
    :nombre (fn [this] "")
    :argumentos (fn [this] (list))
    :aridad (fn [this] 0)
    :verifica? (fn [this premisa preguntar] nil)
})
(extend nil Regla implementacion-mala)
(extend Object Regla implementacion-mala)

;;1- (verifica? quien pregunta repreguntar)
;;2- (verifica? quien pregunta repreguntar anteriores);;todas las preguntas anteriores
;;quien puede ser: 
;; -coleccion de reglas (disjuncion: base de datos) (conjuncion: las de las premisas) (las bases de datos reciben una funci'on que siempre devuelve false)
;; -premisa
;; -inferencia

;;y parsear?
;;
(comment
(def mayusculas (re-pattern "^[A-Z][a-z]*"))
(def minusculas (re-pattern "^[a-z]+"))
(defn- matchea [x y & args] (do
    (println (list x y args))
    [x y]
))
(defmulti gritar matchea)
(defmethod gritar [Object Object] [x y & args] (println  (list "1 y 2" )) )
(defmethod gritar [3 4] [x y & args] (println (list "3 y 4" )) )
(defmethod gritar :default [x y & args] (println "asd"))
)