(ns akiroz.re-frame.storage-tests
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [chan <! put!]]
            [cljs.test :refer-macros [deftest is async]]
            [re-frame.core :refer [dispatch reg-event-fx inject-cofx trim-v]]
            [akiroz.re-frame.storage :as storage :refer [reg-co-fx! persist-db]]
            ))

(deftest fx-cofx-test
  (let [probe-chan (chan)]
    (reg-co-fx! :fx-test-key
                {:fx :ls :cofx :ls}) 
    (reg-event-fx
      :fx-cofx-test
      [trim-v (inject-cofx :ls)]
      (fn [{:keys [ls]} [x]]
        (put! probe-chan (or ls "nil"))
        {:ls x}))
    (dispatch [:fx-cofx-test 1])
    (dispatch [:fx-cofx-test "hello"])
    (dispatch [:fx-cofx-test [:a :b :c]])
    (dispatch [:fx-cofx-test {:d 4 :e 5}])
    (dispatch [:fx-cofx-test nil])
    (async done
           (go
             (is (= (<! probe-chan) "nil"))
             (is (= (<! probe-chan) 1))
             (is (= (<! probe-chan) "hello"))
             (is (= (<! probe-chan) [:a :b :c]))
             (is (= (<! probe-chan) {:d 4 :e 5}))
             (done)))))

(deftest persist-db-test
  (let [probe-chan (chan)]
    (reg-event-fx
      :persist-db-test
      [trim-v (persist-db :db-test-key :p)]
      (fn [{:keys [db]} [x]]
        (put! probe-chan db)
        {:db (assoc db :p x :q "qux")}))
    (dispatch [:persist-db-test 1])
    (dispatch [:persist-db-test "hello"])
    (dispatch [:persist-db-test [:a :b :c]])
    (dispatch [:persist-db-test {:d 4 :e 5}])
    (dispatch [:persist-db-test nil])
    (async done
           (go
             (is (= (<! probe-chan) {:p nil}))
             (is (= (<! probe-chan) {:q "qux" :p 1}))
             (is (= (<! probe-chan) {:q "qux" :p "hello"}))
             (is (= (<! probe-chan) {:q "qux" :p [:a :b :c]}))
             (is (= (<! probe-chan) {:q "qux" :p {:d 4 :e 5}}))
             (done)))))

(deftest persist-db-keys-test
  (let [probe-chan (chan)
        check-storage #(storage/<-store :db-test-key-multi)]
    (reg-event-fx
      :persist-db-keys-test
      [trim-v (storage/persist-db-keys :db-test-key-multi [:p :q])]
      (fn [{:keys [db]} [x y]]
        (put! probe-chan [db (storage/<-store :db-test-key-multi)])
        {:db (assoc db :p x :q y :r "qux")}))
    (dispatch [:persist-db-keys-test 1 2])
    (dispatch [:persist-db-keys-test "hello" :test])
    (dispatch [:persist-db-keys-test [:a :b :c] 3])
    (dispatch [:persist-db-keys-test {:d 4 :e 5} "hi"])
    (dispatch [:persist-db-keys-test nil nil])
    (async done
           (go
             (<! probe-chan)
             (is (= (<! probe-chan) [{:r "qux" :p 1 :q 2}
                                     {:p 1 :q 2}]))
             (is (= (<! probe-chan) [{:r "qux" :p "hello" :q :test}
                                     {:p "hello" :q :test}]))
             (is (= (<! probe-chan) [{:r "qux" :p [:a :b :c] :q 3}
                                     {:p [:a :b :c] :q 3}]))
             (is (= (<! probe-chan) [{:r "qux" :p {:d 4 :e 5} :q "hi"}
                                     {:p {:d 4 :e 5} :q "hi"}]))
             (done)))))

