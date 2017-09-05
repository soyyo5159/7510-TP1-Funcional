(ns Error)
;;inspirado en monads
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
(defmulti error? class)
(defmethod error? :default [yo] false)
(defmethod error? RecordMError [yo] true)
(defmethod error? nil [yo] true)
;;Le podes preguntar a cualquier cosa si es un error, pero no todo tiene un mensaje
;;nil es un error malo

(defn- error-o-false [x] (if (error? x) x false) )

(defn error [x] (RecordMError. x))

(defn fmap [funcion & argumentos] ;;devuelve el primer error, o resultado que corresponde
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
