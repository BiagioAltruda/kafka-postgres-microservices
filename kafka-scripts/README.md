# KAFKA SCRIPTS CHEATSHEET

## this directory contains sripts with better defaults to access the kafka api

### scripts list

Pass the wanted script name as the first argument and directly the flags after it

- topic.sh
  - create
    - -n | --name sets topic name
    - -s | --server sets bootstrap server
    - -p | --partitions sets partitions
    - -r | --replication-factor sets replication factor
  - alter
    - -n | --name sets topic name
    - -s | --server sets bootstrap server
    - -p | --partitions sets partitions, new partition number must be greater than previous
- start.sh
- stop.sh
- configs.sh
- producer.sh
- consumer.sh
- group.sh
