(ns Espia)
;;Este espía simplemente graba todo lo que se le llama y delega la lógica del mock a otro
;;Permite espiar conjuntos de funciones para chequear el orden en que se las llama
;;Si el orden no importe se puede hacer un espía por función
;;el concepto está lindo para extenderlo
(defprotocol Espia
    (envuelta  [this f nombre])
    (registro? [this]))



(defrecord RecordEspia [registro] Espia
    (envuelta [this funcion nombre-funcion]
        "Envuelve la función, la función que se devuelve permite que se espíen los llamados"
        (let [
            agregar-registro (fn [nuevo] 
                (swap!   (:registro this)   (fn [x] (concat x nuevo))  )
            )
        ]
            (fn [& argumentos]
                "funcion envuelta que se devuelve"
                (agregar-registro (cons nombre-funcion argumentos) )
                
                (apply funcion argumentos)
            )
        )
    )

    (registro? [this]
        "devuelve el registro"
        (deref (:registro this))
    )
)

(defn nuevoEspia [] (RecordEspia. (atom (list))))