package rabbitMQTest.timeToLive;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException; 
import com.rabbitmq.client.AMQP.BasicProperties;  
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import rabbitMQTest.common.SerializeUtils;
import rabbitMQTest.common.User;

/** 
 * @author  �  
 * @date ����ʱ�䣺2017��11��17�� ����3:10:38 
 * @version 1.0 
*/
public class Send {
	
	
	/**
	 * ��topicת�����Ļ�������ϰ��ʱת����������Ϣʱָ����Ϣ����ʱ�� 
     * ��Ϣ�ѷ��͵�queue�ϣ���δ��consumer�������� 
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static void SendAtoB(Serializable object) throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		//����ת���Ľ�����
		channel.exchangeDeclare("header_exechange", "topic");
		
		//����headers�洢�ļ�ֵ��
		Map<String, Object> headers = new HashMap<String,Object>();
		headers.put("key", "123456");
		headers.put("token", "654321");
		
		//�Ѽ�ֵ�Է���properties
		 Builder properties=new BasicProperties.Builder();  
         properties.headers(headers);  
         properties.deliveryMode(2);//�־û�  
//	     //ָ����Ϣ����ʱ��Ϊ12��,������Ҳ����ָ����Ϣ�Ĺ���ʱ�䣬�����Խ�Сʱ��Ϊ׼  
//   	 properties.expiration("12000");//��ʱ12�룬���ἰʱɾ��(��consuemr����ʱ�ж��Ƿ���ڣ���Ϊÿ����Ϣ�Ĺ���ʱ�䲻һ�£�ɾ��������Ϣ����Ҫɨ����������)  
	     channel.basicPublish("header_exechange", "" ,properties.build(), SerializeUtils.serialize(object));  
	     System.out.println("Send '"+object+"'");  
	     channel.close();  
	     connection.close();  
	}
	
	/**
	 * @param args
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, TimeoutException {
		User user = new User();
		user.setAge(12);
		user.setName("�12");
		user.setPassword("123456");
		SendAtoB(user);
	}

}
