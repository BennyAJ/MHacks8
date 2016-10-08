# This file is from us, not the library developer
from __future__ import print_function

from collections import Counter
import struct
import sys
import time

import numpy as np

from common import *
import myo

class EMGHandler(object):
    def __init__(self, m):
        self.recording = -1
        self.m = m
        self.emg = (0,) * 8

    def __call__(self, emg, moving):
        self.emg = emg
        if self.recording >= 0:
            self.m.cls.store_data(self.recording, emg)

if __name__ == '__main__':
    m = myo.Myo(myo.NNClassifier(), sys.argv[1] if len(sys.argv) >= 2 else None)
    hnd = EMGHandler(m)
    m.add_emg_handler(hnd)
    m.connect()

    try:

        peaks = []
        for i in range(0, 7):
            peaks.append(i)

        start_time = time.time()
        print(start_time)
        print("Gathering Data")
        
        while time.time() < (start_time + 3):
            m.run()
            for i in range(0, 7):
                if hnd.emg[i] > peaks[i]:
                    peaks[i] = hnd.emg[i]
        
        print(peaks)
            

    except KeyboardInterrupt:
        pass
    finally:
        m.disconnect()
        print()
