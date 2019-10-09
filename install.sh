# Add Openjdk to get Jave
echo vagrant | sudo -S add-apt-repository ppa:openjdk-r/ppa

# Update package list
echo vagrant | sudo -S apt-get update

# Install zip & unzip
apt-get install zip
apt-get install unzip

# Install Java
echo vagrant | sudo -S apt-get install -y openjdk-8-jdk

# Install kafka binary package
cd /home/vagrant
wget https://www-us.apache.org/dist/kafka/2.3.0/kafka_2.12-2.3.0.tgz
tar xzf kafka_2.12-2.3.0.tgz
mv kafka_2.12-2.3.0 kafka
rm kafka_2.12-2.3.0.tgz

# Replace server properties file with socket server settings.
cp server.properties kafka/config/
cd kafka

# Start Zookeeper server
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka server
bin/kafka-server-start.sh config/server.properties

# Create topic
bin/kafka-topics.sh --create --bootstrap-server 172.28.128.4:9092 --replication-factor 1 --partitions 6 --topic test