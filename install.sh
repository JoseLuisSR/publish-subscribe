# Add Openjdk to get Jave
sudo add-apt-repository ppa:openjdk-r/ppa

# Update package list
sudo apt-get update

# Install zip & unzip
apt-get install zip
apt-get install unzip

# Install Java
sudo apt-get install openjdk-8-jdk

# Install kafka binary package
cd /home/vagrant
wget https://www-us.apache.org/dist/kafka/2.3.0/kafka_2.12-2.3.0.tgz
tar xzf kafka_2.12-2.3.0.tgz
mv kafka_2.12-2.3.0 kafka
cd kafka

# Start Zookeeper server
bin/zookeeper-server-start.sh config/zookeeper.properties &

# Start Kafka server
bin/kafka-server-start.sh config/server.properties &

# Create topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 6 --topic test