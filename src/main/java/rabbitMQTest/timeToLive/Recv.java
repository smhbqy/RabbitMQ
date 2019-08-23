package rabbitMQTest.timeToLive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import rabbitMQTest.common.SerializeUtils;
import rabbitMQTest.common.User;

/** 
 * @author  �  
 * @date ����ʱ�䣺2017��11��17�� ����4:01:22 
 * @version 1.0 
*/
public class Recv {
	
	 /** 
     * ��topicת�����Ļ�������ϰ��ʱת�������ö��й���ʱ��(���ں��Զ�ɾ��)��������Ϣ�������(ת������ƥ���queue) 
     * ʵ��ʱ���������ഴ�����к󣬹رո��̣߳�ʹ�����δʹ��״̬ 
     * @throws Exception 
     */  
    public static void recvAToB() throws Exception{  
    	ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
        channel.exchangeDeclare("header_exechange", "topic");  
        
        //���ö��й���ʱ��Ϊ30�룬��Ϣ����ת����ָ��ת������ƥ���routingkey(�ɲ�ָ��)  
        Map<String, Object> args=new HashMap<String, Object>();  
        args.put("x-expires", 30000l);//���й���ʱ��  
        args.put("x-message-ttl", 12000l);//��������Ϣ����ʱ��  
        args.put("x-dead-letter-exchange", "exchange-direct");//������Ϣת��·��  
        args.put("x-dead-letter-routing-key", "routing-delay");//������Ϣת��·����ƥ��routingkey  
        
        //����һ����ʱ����  
        String queueName=channel.queueDeclare("tmp01",true,false,false,args).getQueue();  
       
        //ָ��headers��ƥ������(all��any)����ֵ��  
        Map<String, Object> headers=new HashMap<String, Object>();  
        headers.put("x-match", "all");//all any(ֻҪ��һ����ֵ��ƥ�伴��)  
        headers.put("key", "123456");  
//      headers.put("token", "6543211");  
        
        //����ʱ���к�ת����header_exchange  
        channel.queueBind(queueName, "header_exechange", "", headers);  
        System.out.println("����Received ...");
//        Consumer consumer=new DefaultConsumer(channel){  
//            @Override  
//            public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException{  
//                User user = (User)SerializeUtils.deSerialize(body);  
//                System.out.println(envelope.getRoutingKey()+":Received :'"+user.toString()+"' done");  
//                channel.basicAck(envelope.getDeliveryTag(), false);  
//            }  
//        };  
       
//        //�ر��Զ�Ӧ����ƣ�Ĭ�Ͽ�������ʱ����Ҫ�ֶ�����
//        channel.basicConsume(queueName, true, consumer);  
    }  
      
    public static void main(String[] args) throws Exception {  
        recvAToB();  
    }  

}
