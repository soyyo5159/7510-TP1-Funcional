(ns Error)
;;inspirado en monads

;El protocolo sólo lo cumplen aquellas cosas que son errores: 
;RecordMError y nil. El resto no es un error.

(defprotocol ^:private MError
    (mensaje? [yo])
)
(defrecord ^:private RecordMError [mensaje] 
    MError
    (mensaje? [yo] mensaje)
)
(extend nil MError 
    {
        :mensaje? (fn [yo] "sin mensaje")
    }
)

;;A cualquier cosa se le puede preguntar si es un error, con lo cual
;;esta función no corresponde al protocolo anterior
(defmulti error? class)
(defmethod error? :default [yo] false)
(defmethod error? RecordMError [yo] true)
(defmethod error? nil [yo] true)

(defn- error-o-false [x] (if (error? x) x false) )

(defn error [x] (->RecordMError x))

(defn fmap [funcion & argumentos] 
    "Si algun parametro es un error, lo devuelve
    Si ningun parámetro es un error, ejecuta la función.
    Pudiendo devolver un error o un valor"
    (if-let [
            error (some error-o-false argumentos)
        ]
        error
        (try
            (apply funcion argumentos)
            (catch Exception e (error (.getMessage e))) 
            (catch Error e  (error (.getMessage e))) 
        )
        
    )
)
