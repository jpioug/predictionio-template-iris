# -*- coding: utf-8 -*-

"""
Import sample data for classification engine
"""

import predictionio
import argparse
import os
import sys

def import_events(client, file):
  with open(file, 'r') as f:
    count = 0
    print("Importing data...")
    for line in f:
      data = line.rstrip('\r\n').split(",")
      if len(data) == 5:
        client.create_event(
          event="$set",
          entity_type="user",
          entity_id=str(count), # use the count num as user ID
          properties= {
            "attr0" : float(data[0]),
            "attr1" : float(data[1]),
            "attr2" : float(data[2]),
            "attr3" : float(data[3]),
            "target" : data[4]
          }
        )
        count += 1
  print("%s events are imported." % count)

def download_datafile(url, path):
  if sys.version_info[0] >= 3:
    from urllib.request import urlretrieve
  else:
    from urllib import urlretrieve
  urlretrieve(url, path)

if __name__ == '__main__':
  parser = argparse.ArgumentParser(
    description="Import sample data")
  parser.add_argument('--access-key', dest='access_key', default='IRIS_TOKEN')
  parser.add_argument('--url', default="http://localhost:7070")
  parser.add_argument('--file', dest='file_path', default="./data/iris.data")
  parser.add_argument('--download-url', dest='download_url',
    default="https://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data")

  args = parser.parse_args()
  print(args)

  if not os.path.exists(args.file_path):
    download_datafile(args.download_url, args.file_path)

  client = predictionio.EventClient(
    access_key=args.access_key,
    url=args.url,
    threads=5,
    qsize=500)
  import_events(client, args.file_path)

