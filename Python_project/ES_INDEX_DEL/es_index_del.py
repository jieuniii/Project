'''
Created on 2018. 1. 9.

@author: Direa56
'''
from apscheduler.schedulers.blocking import BlockingScheduler
import datetime
import time
import elasticsearch
import re
import logging.handlers

logging.basicConfig()
logger = logging.getLogger('DeepchannelLogger')
logger.setLevel(logging.DEBUG)
formatter = logging.Formatter('[%(asctime)s] [%(levelname)s] %(message)s')

fileHandler = logging.FileHandler('./ES_INDEX_DELETE.log')
fileHandler.setFormatter(formatter)
logger.addHandler(fileHandler)

streamHandler = logging.StreamHandler()
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

def job_function():
    T = datetime.date.fromtimestamp(time.time())
    t = datetime.timedelta(days=-7)
    es_client = elasticsearch.Elasticsearch("")

    for index in es_client.indices.get("*"):

        m = re.match('.+[0-9]{4}.[0-9]{2}.[0-9]{2}', index)
        if m:
            #print(m.group())
            m2 = str(m.group())
            D = datetime.date(int(m2[-10:-6]), int(m2[-5:-3]), int(m2[-2:]))
            if D < T+t:
                es_client.indices.delete(index=m2)
                message = m2 + " Index Delete!"
                logger.debug(message)
                

sched = BlockingScheduler()

#sched.add_job(job_function, 'cron', hour=11)
sched.add_job(job_function, 'interval', seconds=10)

if __name__ == '__main__':

    sched.start()

                
            