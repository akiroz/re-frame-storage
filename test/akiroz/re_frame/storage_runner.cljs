(ns akiroz.re-frame.storage-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [akiroz.re-frame.storage-tests]
            ))

(enable-console-print!)
(doo-tests 'akiroz.re-frame.storage-tests)
