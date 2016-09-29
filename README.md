# re-frame-storage



A very simple re-frame wrapper around [storage-atom][storage-atom] for persisting app state.

Depends on `re-frame >= 0.8.0`. 


## Usage

There are 2 ways to use this:

### 1. As individual re-frame fx & cofx

```clojure
(ns my-app
  (:require [akiroz.re-frame.storage :refer [reg-co-fx!]]))

;; both :fx and :cofx keys are optional, they will not be registered if unspecified.
(reg-co-fx! :my-app         ;; local storage key
            {:fx :store     ;; re-frame fx ID
             :cofx :store}) ;; re-frame cofx ID

;; ...

(reg-event-fx
  :read-foo-store-bar
  [(inject-cofx :store)]
  (fn [{keys [store]} _]
    (print (:foo store))
    {:store (assoc store :bar "qux")}))

```

### 2. As a re-frame interceptor that automatically persists part of your app-db

```clojure
(ns my-app
  (:require [akiroz.re-frame.storage :refer [persist-db]]))

;; define a custum reg-event-db
;; local storage key is :my-app
;; everything inside the db's :persistant key is automatically stored and retreived.
(defn my-reg-event-db
  [event-id handler]
  (reg-event-fx
    event-id
    [(persist-db :my-app :persistant)]
    (fn [{:keys [db]} event-vec]
      {:db (handler db event-vec)})))

;; ...

(my-reg-event-db
  :read-foo-store-bar
  (fn [db _]
    (print (get-in db [:persistant :foo]))
    (assoc-in db [:persistant :bar] "qux")))

```

[storage-atom]: https://github.com/alandipert/storage-atom
