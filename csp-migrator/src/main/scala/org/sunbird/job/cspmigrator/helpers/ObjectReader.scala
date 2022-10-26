package org.sunbird.job.cspmigrator.helpers

import com.datastax.driver.core.Row
import com.datastax.driver.core.querybuilder.{QueryBuilder, Select}
import org.apache.commons.lang3
import org.slf4j.LoggerFactory
import org.sunbird.job.cspmigrator.task.CSPMigratorConfig
import org.sunbird.job.util.{CassandraUtil, Neo4JUtil}

import scala.collection.JavaConverters._

trait ObjectReader {

  private[this] val logger = LoggerFactory.getLogger(classOf[ObjectReader])
  val extProps = List("body", "editorState", "answer", "solutions", "instructions", "hints", "media", "responseDeclaration", "interactions", "identifier")

  def getMetadata(identifier: String, pkgVersion: Double)(implicit neo4JUtil: Neo4JUtil): Map[String, AnyRef] = {
    val nodeId = getEditableObjId(identifier, pkgVersion)
    val metaData = Option(neo4JUtil.getNodeProperties(nodeId)).getOrElse(neo4JUtil.getNodeProperties(identifier)).asScala.toMap
    val id = metaData.getOrElse("IL_UNIQUE_ID", identifier).asInstanceOf[String]
    val objType = metaData.getOrElse("IL_FUNC_OBJECT_TYPE", "").asInstanceOf[String]
    logger.info("ObjectReader:: getMetadata:: identifier: " + identifier + " with objType: " + objType)
    metaData ++ Map[String, AnyRef]("identifier" -> id, "objectType" -> objType) - ("IL_UNIQUE_ID", "IL_FUNC_OBJECT_TYPE", "IL_SYS_NODE_TYPE")
  }

  def getEditableObjId(identifier: String, pkgVersion: Double): String = {
    if (pkgVersion > 0) identifier + ".img" else identifier
  }

  def getLiveNodeMetadata(identifier: String)(implicit neo4JUtil: Neo4JUtil): Map[String, AnyRef] = {
    val metaData = Option(neo4JUtil.getNodeProperties(identifier)).getOrElse(neo4JUtil.getNodeProperties(identifier)).asScala.toMap
    val id = metaData.getOrElse("IL_UNIQUE_ID", identifier).asInstanceOf[String]
    val objType = metaData.getOrElse("IL_FUNC_OBJECT_TYPE", "").asInstanceOf[String]
    logger.info("ObjectReader:: getLiveNodeMetadata:: identifier: " + identifier + " with objType: " + objType)
    metaData ++ Map[String, AnyRef]("identifier" -> id, "objectType" -> objType) - ("IL_UNIQUE_ID", "IL_FUNC_OBJECT_TYPE", "IL_SYS_NODE_TYPE")
  }

  def getContentBody(identifier: String, config: CSPMigratorConfig)(implicit cassandraUtil: CassandraUtil): String = {
    // fetch content body from cassandra
    val selectId = QueryBuilder.select()
    selectId.fcall("blobAsText", QueryBuilder.column("body")).as("body")
    val selectWhereId: Select.Where = selectId.from(config.contentKeyspaceName, config.contentTableName).where().and(QueryBuilder.eq("content_id", identifier))
    logger.info("ObjectReader:: getContentBody:: ECML Body Fetch Query :: " + selectWhereId.toString)
    val rowId = cassandraUtil.findOne(selectWhereId.toString)
    if (null != rowId) rowId.getString("body") else ""
  }

  def getQuestionData(identifier: String, config: CSPMigratorConfig)(implicit cassandraUtil: CassandraUtil): Row = {
    logger.info("ObjectReader ::: getQuestionData ::: Reading Question External Data For : " + identifier)
    val select = QueryBuilder.select()
    extProps.foreach(prop => if (lang3.StringUtils.equals("body", prop) | lang3.StringUtils.equals("answer", prop)) select.fcall("blobAsText", QueryBuilder.column(prop.toLowerCase())).as(prop.toLowerCase()) else select.column(prop.toLowerCase()).as(prop.toLowerCase()))
    val selectWhere: Select.Where = select.from(config.contentKeyspaceName, config.assessmentTableName).where().and(QueryBuilder.eq("identifier", identifier))
    logger.info("Cassandra Fetch Query :: " + selectWhere.toString)
    cassandraUtil.findOne(selectWhere.toString)
  }

  def getCollectionHierarchy(identifier: String, config: CSPMigratorConfig)(implicit cassandraUtil: CassandraUtil): String = {
    val selectId = QueryBuilder.select()
    selectId.fcall("blobAsText", QueryBuilder.column("hierarchy")).as("hierarchy")
    val selectWhereId: Select.Where = selectId.from(config.hierarchyKeyspaceName, config.hierarchyTableName).where().and(QueryBuilder.eq("identifier", identifier))
    logger.info("ObjectReader:: getCollectionHierarchy:: Hierarchy Fetch Query :: " + selectWhereId.toString)
    val rowId = cassandraUtil.findOne(selectWhereId.toString)
    if (null != rowId) rowId.getString("hierarchy") else ""
  }

}
