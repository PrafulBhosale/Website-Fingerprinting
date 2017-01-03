import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.SparkContext
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import java.lang._
import java.io.File
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.feature.Normalizer
import org.apache.spark.mllib.util.MLUtils
/**
 * @author Group No. 4
 */
object KafkaConsumerDT {
  
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("KafkaNaiveBayes")
    sparkConf.setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val ssc = new StreamingContext(sc,Seconds(1))
    ssc.checkpoint("checkpoint")

    val Array(zkQuorum, groupName, topicName, numOfThreads, nameOfModel) = args
    val mappedTopic = topicName.split(",").map((_,numOfThreads.toInt)).toMap
    val DTmodel = DecisionTreeModel.load(sc, nameOfModel)

 
    val currentLine = KafkaUtils.createStream(ssc, zkQuorum, groupName,  mappedTopic).map(_._2)
    val trainData = currentLine.window(Seconds(10)).map { x =>
       val splitParts = x.split(',')
       LabeledPoint(splitParts(0).toCharArray()(7).toDouble-48, Vectors.dense(splitParts(1).split(' ').map(_.toDouble).take(873)))
     }
     val normalizer = new Normalizer()
//preditc
     val predictionAndLabel = trainData.map(p => (DTmodel.predict(normalizer.transform(p.features)), p.label))
//print predictions and actual labels
    
    var total = 0.0
    var trueValues = 0.0
    var falseValues = 0.0

     predictionAndLabel.foreach(x=>{
         x.collect().foreach(x=>{
            println(x._1+"  "+x._2)
            total = total + 1.0
            if(x._1 == x._2)
                trueValues = trueValues + 1.0
            else
                falseValues = falseValues + 1.0
         })
        if(total>0.0)
        {
         println("Accuracy of Decision tree is: "+(trueValues/total)*100 + "%")
         println("Error of Decision tree is: "+(falseValues/total)*100 + "%")
     }
     })

    ssc.start()
    ssc.awaitTermination()
    
  }

}
