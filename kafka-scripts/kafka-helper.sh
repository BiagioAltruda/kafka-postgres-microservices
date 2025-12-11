#!/bin/bash

base_bootstrap_server='localhost:9092'
kafka_bin_location="$HOME/kafka_2.13-4.1.1/bin"

# This script is used to create, modify and delete topics from kafka running server

main() {
  case $1 in
  create | alter | describe | delete)
    create-command "kafka-topics.sh" "$@"
    ;;
  produce)
    create-command "kafka-console-producer.sh" "$@"
    ;;
  consume)
    create-command "kafka-console-consumer.sh" "$@"
    ;;
  *)
    echo "Unknown command: $1, check the documentation in README.md"
    if [[ -f README.md ]]; then
      cat README.md
    fi
    exit 1
    ;;
  esac
}

create-command() {
  local script_name="$1"
  local action="$2"
  local topic_name=''
  local partitions=1
  local replication_factor=1
  local bootstrap_server=''
  local base_command=()

  # Build base command
  base_command+=("$kafka_bin_location/$script_name")

  # Add action flag (--create, --alter, etc.) unless it's "produce"
  local action_group_IO=("produce" "consume")
  local action_group_topic("create" "alter" "describe" "delete")
  if ! printf '%s\n' "${action_group_IO[@]}" | grep -qx "$action"; then
    base_command+=("--$action")
  fi

  shift 2

  # Parse optional arguments for first action group
  if [[ printf '%s\n' "${action_group_topic[@]}" | grep -qx "$action" ]]; then
    while [[ "$1" =~ ^- && "$1" != "--" ]]; do
      case "$1" in
      -n | --name)
        base_command+=(--topic "$2")
        topic_name="$2"
        shift 2
        ;;
      -s | --server)
        base_command+=(--bootstrap-server "$2")
        bootstrap_server="$2"
        shift 2
        ;;
      -p | --partitions)
        base_command+=(--partitions "$2")
        partitions="$2"
        shift 2
        ;;
      -r | --replication-factor)
        base_command+=(--replication-factor "$2")
        replication_factor="$2"
        shift 2
        ;;
      *)
        echo "Unknown argument: $1, check the documentation in README.md"
        if [[ -f README.md ]]; then
          cat README.md
        fi
        exit 1
        ;;
      esac
    done
   
  fi
  if [[ "$action" == "consume" ]]; then
    while [[ "$1" =~ ^- && "$1" != "--" ]]; do
      case "$1" in
      -n | --name)
        base_command+=(--topic "$2")
        topic_name="$2"
        shift 2
        ;;
      -s | --server)
        base_command+=(--bootstrap-server "$2")
        bootstrap_server="$2"
        shift 2
        ;;
      --from-beginning)
        base_command+=(--from-beginning)
        shift
        ;;
      --format)
        base_command+=(--format "$2")
        shift 2
        ;;
      --default-formatter)
        base_command+=(--formatter kafka.tools.DefaultMessageFormatter)
        shift
        ;;
      --with-timestamp)
        base_command+=(--property print.timestamp=true)
        shift
        ;;
      --as-pairs)
        base_command+=(--property print.key=true --property print.value=true --property key.separator=' : ')
        shift
        ;;
      -g | --group-id)
        base_command+=(--consumer-property group.id = "$2")
        shift 2
        ;;
      -p | --listen-partition)
        base_command+=(--partition "$2")
        shift 2
        ;;
      *)
        echo "Unknown argument: $1, check the documentation in README.md"
        if [[ -f README.md ]]; then
          cat README.md
        fi
        exit 1
        ;;
      esac
    done
   
  fi
  # If the positional argument is "--", remove it
  if [[ "$1" == '--' ]]; then
    shift
  fi

  # Add default bootstrap server if not provided
  if [[ -z "$bootstrap_server" ]]; then
    base_command+=(--bootstrap-server "$base_bootstrap_server")
  fi

  # Add any remaining arguments
  if [[ $# -gt 0 ]]; then
    base_command+=("$@")
  fi

  # Execute the command
  echo "Executing: ${base_command[*]}"
  "${base_command[@]}"
}

main "$@"
