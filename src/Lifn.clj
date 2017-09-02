(ns Lifn)
;;una función que es definida por una lista. Cambia cada vez que se la llama, ignora argumentos
(defprotocol Lifn 
    (funcion [this])
    (completa? [this])
)
;;Mejora posible: implementar la interfaz IFn
(defrecord RecordLifn [valores final] Lifn
    (funcion  [this] "devuelve la función que representa" (fn [& args]
        (let [cabeza (first (deref (:valores this)))]
            (swap! (:valores this) rest)
            (or cabeza final)
        )
    ))
    (completa? [this] "true sii se llamaron todos los valores" (empty? (deref (:valores this))))
)



(defn nuevoLifn 
    "genera un Lifn"
    [& args]
    (RecordLifn. (atom args) (last args))
)

(defn nuevoLifn-y-final
    "genera un Lifn"
    [lista final]
    (RecordLifn. (atom lista) (last final))
)