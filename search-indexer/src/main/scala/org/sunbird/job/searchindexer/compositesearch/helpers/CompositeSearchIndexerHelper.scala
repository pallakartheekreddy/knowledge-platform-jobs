package org.sunbird.job.searchindexer.compositesearch.helpers

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.sunbird.job.domain.`object`.{DefinitionCache, ObjectDefinition}
import org.sunbird.job.searchindexer.models.CompositeIndexer
import org.sunbird.job.util.{ElasticSearchUtil, ScalaJsonUtil}

import scala.collection.JavaConverters._

trait CompositeSearchIndexerHelper {

  private[this] val logger = LoggerFactory.getLogger(classOf[CompositeSearchIndexerHelper])

  def createCompositeSearchIndex()(esUtil: ElasticSearchUtil): Boolean = {
    val settings = """{"max_ngram_diff":"29","mapping":{"total_fields":{"limit":"1500"}},"analysis":{"filter":{"mynGram":{"token_chars":["letter","digit","whitespace","punctuation","symbol"],"min_gram":"1","type":"nGram","max_gram":"30"}},"analyzer":{"cs_index_analyzer":{"filter":["lowercase","mynGram"],"type":"custom","tokenizer":"standard"},"keylower":{"filter":"lowercase","tokenizer":"keyword"},"cs_search_analyzer":{"filter":["standard","lowercase"],"type":"custom","tokenizer":"standard"}}}}"""
    val mappings = """{"dynamic_templates":[{"nested":{"match_mapping_type":"object","mapping":{"fields":{"type":"nested"},"type":"nested"}}},{"longs":{"match_mapping_type":"long","mapping":{"fields":{"raw":{"type":"long"}},"type":"long"}}},{"booleans":{"match_mapping_type":"boolean","mapping":{"fields":{"raw":{"type":"boolean"}},"type":"boolean"}}},{"doubles":{"match_mapping_type":"double","mapping":{"fields":{"raw":{"type":"double"}},"type":"double"}}},{"dates":{"match_mapping_type":"date","mapping":{"fields":{"raw":{"type":"date"}},"type":"date"}}},{"strings":{"match_mapping_type":"string","mapping":{"analyzer":"cs_index_analyzer","copy_to":"all_fields","fields":{"raw":{"fielddata":true,"analyzer":"keylower","type":"text"}},"search_analyzer":"cs_search_analyzer","type":"text"}}}],"properties":{"IL_FUNC_OBJECT_TYPE":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"IL_SYS_NODE_TYPE":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"IL_UNIQUE_ID":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"SYS_INTERNAL_LAST_UPDATED_ON":{"type":"date","fields":{"raw":{"type":"date"}}},"additionalCategories":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"all_fields":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"appIcon":{"type":"text","index":false},"appId":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"artifactUrl":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"assets":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"associations":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"associationswith":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"audience":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"author":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"autoCreateBatch":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"batches":{"type":"nested","properties":{"batchId":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"createdFor":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"enrollmentType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"name":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"startDate":{"type":"date","fields":{"raw":{"type":"date"}}},"status":{"type":"long","fields":{"raw":{"type":"long"}}}}},"board":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"body":{"type":"text","index":false},"categories":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"category":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"categoryId":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"channel":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"channels":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"childNodes":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"cloudStorageKey":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"code":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"compatibilityLevel":{"type":"long","fields":{"raw":{"type":"long"}}},"consumerId":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"contentDisposition":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"contentEncoding":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"contentType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"contentTypesCount":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"copyright":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"copyrightYear":{"type":"long","fields":{"raw":{"type":"long"}}},"createdBy":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"createdFor":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"createdOn":{"type":"date","fields":{"raw":{"type":"date"}}},"creator":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"credentials":{"type":"nested","properties":{"enabled":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"}}},"defaultCourseFramework":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"defaultFramework":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"defaultLicense":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"depth":{"type":"long","fields":{"raw":{"type":"long"}}},"description":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"dialcodeRequired":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"dialcodes":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"discussionForum":{"type":"nested","properties":{"enabled":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"}}},"downloadUrl":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"editorState":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"framework":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"frameworks":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"generateDIALCodes":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"gradeLevel":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"graph_id":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"idealScreenDensity":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"idealScreenSize":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"identifier":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"index":{"type":"long","fields":{"raw":{"type":"long"}}},"interceptionPoints":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"language":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"languageCode":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"lastPublishedBy":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"lastPublishedOn":{"type":"date","fields":{"raw":{"type":"date"}}},"lastStatusChangedOn":{"type":"date","fields":{"raw":{"type":"date"}}},"lastSubmittedOn":{"type":"date","fields":{"raw":{"type":"date"}}},"lastUpdatedBy":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"lastUpdatedOn":{"type":"date","fields":{"raw":{"type":"date"}}},"leafNodes":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"leafNodesCount":{"type":"long","fields":{"raw":{"type":"long"}}},"license":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"lockKey":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"mediaType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"medium":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"mimeType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"mimeTypesCount":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"name":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"nodeType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"node_id":{"type":"long","fields":{"raw":{"type":"long"}}},"objectType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"orgIdFieldName":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"organisation":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"os":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"osId":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"ownershipType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"parent":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"pkgVersion":{"type":"long","fields":{"raw":{"type":"long"}}},"plugins":{"type":"nested","properties":{"identifier":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"semanticVersion":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"}}},"pragma":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"prevState":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"prevStatus":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"previewUrl":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"primaryCategory":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"publishError":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"publish_type":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"reservedDialcodes":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"resourceType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"s3Key":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"sYS_INTERNAL_LAST_UPDATED_ON":{"type":"date","fields":{"raw":{"type":"date"}}},"screenshots":{"type":"text","index":false},"se_FWIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"se_boardIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"se_boards":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"se_gradeLevelIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"se_gradeLevels":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"se_mediumIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"se_mediums":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"se_subjectIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"se_subjects":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"searchIdFieldName":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"searchLabelFieldName":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"size":{"type":"double","fields":{"raw":{"type":"double"}}},"status":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"streamingUrl":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"subject":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"subjectIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"systemDefault":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"targetBoardIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"targetFWIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"targetGradeLevelIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"targetIdFieldName":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"targetMediumIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"targetObjectType":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"targetSubjectIds":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"terms":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"toc_url":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"totalCompressedSize":{"type":"long","fields":{"raw":{"type":"long"}}},"totalQuestions":{"type":"long","fields":{"raw":{"type":"long"}}},"totalScore":{"type":"long","fields":{"raw":{"type":"long"}}},"trackable":{"type":"nested","properties":{"autoBatch":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"enabled":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"}}},"type":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"url":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"userConsent":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"variants":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"version":{"type":"long","fields":{"raw":{"type":"long"}}},"versionKey":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"},"visibility":{"type":"text","fields":{"raw":{"type":"text","analyzer":"keylower","fielddata":true}},"copy_to":["all_fields"],"analyzer":"cs_index_analyzer","search_analyzer":"cs_search_analyzer"}}}"""
    esUtil.addIndex(settings, mappings)
  }

  private def getIndexDocument(identifier: String)(esUtil: ElasticSearchUtil): scala.collection.mutable.Map[String, AnyRef] = {
    val documentJson: String = esUtil.getDocumentAsString(identifier)
    val indexDocument = if (documentJson != null && documentJson.nonEmpty) ScalaJsonUtil.deserialize[scala.collection.mutable.Map[String, AnyRef]](documentJson) else scala.collection.mutable.Map[String, AnyRef]()
    indexDocument
  }

  def getIndexDocument(message: Map[String, Any], isUpdate: Boolean, definition: ObjectDefinition, nestedFields: List[String], ignoredFields: List[String])(esUtil: ElasticSearchUtil): Map[String, AnyRef] = {
    val identifier = message.getOrElse("nodeUniqueId", "").asInstanceOf[String]
    val indexDocument = if (isUpdate) getIndexDocument(identifier)(esUtil) else scala.collection.mutable.Map[String, AnyRef]()
    val transactionData = message.getOrElse("transactionData", Map[String, Any]()).asInstanceOf[Map[String, Any]]

    if (transactionData.nonEmpty) {
      val addedProperties = transactionData.getOrElse("properties", Map[String, AnyRef]()).asInstanceOf[Map[String, AnyRef]]
      addedProperties.foreach(property => {
        if (!definition.externalProperties.contains(property._1)) {
          val propertyNewValue: AnyRef = property._2.asInstanceOf[Map[String, AnyRef]].getOrElse("nv", null) match {
            case propVal: List[AnyRef] => if(propVal.isEmpty) null else propVal
            case _ => property._2.asInstanceOf[Map[String, AnyRef]].getOrElse("nv", null)
          }
          if (propertyNewValue == null) indexDocument.remove(property._1) else indexDocument.put(property._1, addMetadataToDocument(property._1, propertyNewValue, nestedFields))
        }
      })

      val addedRelations = transactionData.getOrElse("addedRelations", List[Map[String, AnyRef]]()).asInstanceOf[List[Map[String, AnyRef]]]
      if (addedRelations.nonEmpty) {
        addedRelations.foreach(rel => {
          val direction = rel.getOrElse("dir", "").asInstanceOf[String]
          val relationType = rel.getOrElse("rel", "").asInstanceOf[String]
          val targetObjType = rel.getOrElse("type", "").asInstanceOf[String]
          val title = definition.relationLabel(targetObjType, direction, relationType)
          if (title.nonEmpty) {
            val list = indexDocument.getOrElse(title.get, List[String]()).asInstanceOf[List[String]]
            val id = rel.getOrElse("id", "").asInstanceOf[String]
            if (!list.contains(id)) indexDocument.put(title.get, (id :: list).asInstanceOf[AnyRef])
          }
        })
      }

      val removedRelations = transactionData.getOrElse("removedRelations", List[Map[String, AnyRef]]()).asInstanceOf[List[Map[String, AnyRef]]]
      removedRelations.foreach(rel => {
        val direction = rel.getOrElse("dir", "").asInstanceOf[String]
        val relationType = rel.getOrElse("rel", "").asInstanceOf[String]
        val targetObjType = rel.getOrElse("type", "").asInstanceOf[String]
        val title = definition.relationLabel(targetObjType, direction, relationType)
        if (title.nonEmpty) {
          val list = indexDocument.getOrElse(title.get, List[String]()).asInstanceOf[List[String]]
          val id = rel.getOrElse("id", "").asInstanceOf[String]
          if (list.contains(id)) {
            val updatedList =  list diff List(id)
            indexDocument.put(title.get, updatedList.asInstanceOf[AnyRef])
          }
        }
      })
    }
    
    //Ignored fields are removed-> it can be a propertyName or relation Name
    indexDocument --= ignoredFields

    indexDocument.put("graph_id", message.getOrElse("graphId", "").asInstanceOf[String])
    indexDocument.put("node_id", message.getOrElse("nodeGraphId", 0).asInstanceOf[Integer])
    indexDocument.put("identifier", message.getOrElse("nodeUniqueId", "").asInstanceOf[String])
    indexDocument.put("objectType", message.getOrElse("objectType", "").asInstanceOf[String])
    indexDocument.put("nodeType", message.getOrElse("nodeType", "").asInstanceOf[String])
    indexDocument.toMap
  }

  def upsertDocument(identifier: String, jsonIndexDocument: String)(esUtil: ElasticSearchUtil): Unit = {
    esUtil.addDocument(identifier, jsonIndexDocument)
  }

  def processESMessage(compositeObject: CompositeIndexer)(esUtil: ElasticSearchUtil, defCache: DefinitionCache): Unit = {
    val definition = defCache.getDefinition(compositeObject.objectType, compositeObject.getVersionAsString(), compositeObject.getDefinitionBasePath())

    val compositeMap = compositeObject.message.asScala.toMap
    upsertDocument(compositeObject.identifier, compositeMap, definition, compositeObject.getNestedFields(), compositeObject.getIgnoredFields())(esUtil)
  }


  private def upsertDocument(identifier: String, message: Map[String, Any], definition: ObjectDefinition, nestedFields: List[String], ignoredFields: List[String])(esUtil: ElasticSearchUtil): Unit = {
    val operationType = message.getOrElse("operationType", "").asInstanceOf[String]
    operationType match {
      case "CREATE" =>
        val indexDocument = getIndexDocument(message, false, definition, nestedFields, ignoredFields)(esUtil)
        val jsonIndexDocument = ScalaJsonUtil.serialize(indexDocument)
        upsertDocument(identifier, jsonIndexDocument)(esUtil)
      case "UPDATE" =>
        val indexDocument = getIndexDocument(message, true, definition, nestedFields, ignoredFields)(esUtil)
        val jsonIndexDocument = ScalaJsonUtil.serialize(indexDocument)
        upsertDocument(identifier, jsonIndexDocument)(esUtil)
      case "DELETE" =>
        val id = message.getOrElse("nodeUniqueId", "").asInstanceOf[String]
        val indexDocument = getIndexDocument(id)(esUtil)
        val visibility = indexDocument.getOrElse("visibility", "").asInstanceOf[String]
        if (StringUtils.equalsIgnoreCase("Parent", visibility)) logger.info(s"Not deleting the document (visibility: Parent) with ID: $id")
        else esUtil.deleteDocument(identifier)
      case _ =>
        logger.info(s"Unknown Operation Type : $operationType for the identifier: $identifier.")
    }
  }

  private def addMetadataToDocument(propertyName: String, propertyValue: AnyRef, nestedFields: List[String]): AnyRef = {
    val propertyNewValue = if (nestedFields.contains(propertyName)) ScalaJsonUtil.deserialize[AnyRef](propertyValue.asInstanceOf[String]) else propertyValue
    propertyNewValue
  }

}
