# re-frame-storage

[![Clojars Project](https://img.shields.io/clojars/v/akiroz.re-frame/storage.svg)](https://clojars.org/akiroz.re-frame/storage)
[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/akiroz/re-frame-storage/master/LICENSE)
[![Build Status](https://travis-ci.org/akiroz/re-frame-storage.svg?branch=master)](https://travis-ci.org/akiroz/re-frame-storage)

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
  (fn [{:keys [store]} _]
    (print (:foo store))
    {:store (assoc store :bar "qux")}))

```

### 2. As a re-frame interceptor that automatically persists part of your app-db

```clojure
(ns my-app
  (:require [akiroz.re-frame.storage :refer [persist-db]]))

;; define a custom reg-event-db
;; local storage key is :my-app
;; everything inside the db's :persistent key is automatically stored and retreived.
(defn my-reg-event-db
  [event-id handler]
  (reg-event-fx
    event-id
    [(persist-db :my-app :persistent)]
    (fn [{:keys [db]} event-vec]
      {:db (handler db event-vec)})))

;; ...

(my-reg-event-db
  :read-foo-store-bar
  (fn [db _]
    (print (get-in db [:persistent :foo]))
    (assoc-in db [:persistent :bar] "qux")))

;; Or, if you want to persist multiple DB keys:

(defn my-reg-event-db
  [event-id handler]
  (reg-event-fx
    event-id
    [(persist-db-keys :my-app [:persistent-1 :persistent-2])]
    (fn [{:keys [db]} event-vec]
      {:db (handler db event-vec)})))

;; May want an init helper to dispatch on app init

(my-reg-event-db :init (fn [db] db))

```

[storage-atom]: https://github.com/alandipert/storage-atom
