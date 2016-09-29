(ns akiroz.re-frame.storage
  (:require [re-frame.core :refer [reg-fx reg-cofx ->interceptor]]
            [alandipert.storage-atom :refer [local-storage]]))

;; atom containing local-storage atoms
(def storage-atoms (atom {}))

(defn register-store [store-key]
  (when-not (@storage-atoms store-key)
    (swap! storage-atoms assoc store-key
           (local-storage (atom nil) store-key))))


(defn reg-co-fx! [store-key {:keys [fx cofx]}]
  (register-store store-key)
  (when fx
    (reg-fx
      fx
      #(reset! (@storage-atoms store-key) %)))
  (when cofx
    (reg-cofx
      cofx
      #(assoc % cofx @(@storage-atoms store-key)))))


(defn persist-db [store-key db-key]
  (register-store store-key)
  (->interceptor
    :id (keyword (str db-key "->" store-key))
    :before (fn [context]
              (assoc-in context [:coeffects :db db-key]
                        @(@storage-atoms store-key)))
    :after (fn [context]
             (when-let [value (get-in context [:effects :db db-key])]
               (reset! (@storage-atoms store-key) value))
             context)))
