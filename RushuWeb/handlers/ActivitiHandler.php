<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-10-9
 * Time: 下午4:46
 */
require_once(getcwd()."/libs/HttpClient.class.php");
class ActivitiHandler {
    const BASE_URL = 'http://www.rushucloud.com:90/activiti-rest/service/';
    const ADMIN_USERNAME = 'kermit';

    private $_services = array(
        'processDefinitions' => 'process-definitions',
        'processDefinition' => 'process-definition/',
        'processInstance' => 'process-instance',
        'processInstances' => 'process-instances',
        'processInstanceDiagram' => 'processInstance/%s/diagram',
        'tasksSummary' => 'tasks-summary?user=%s',
        'tasks' => 'tasks?',
        'task' => 'task/%s'
    );

    /**
     * @var \Http_Client
     */
    private $_client;

    public function __construct($username = self::ADMIN_USERNAME, $password = self::ADMIN_USERNAME){
        $this->_client = new HttpClient(self::BASE_URL,90);
        $this->_client->headers['content-type'] = 'application/json';
        $this->_client->setAuthorization($username, $password);
//        $this->_client->setHeaders('content-type','application/json');
    }


    public function getProcessDefinitions()
    {
        $url = $this->_getServiceUrl('processDefinitions');
        $this->_client->setUri($url);
        return $this->_getResponse();
    }

    public function getProcessDefinition($process_id)
    {
        $url = $this->_getServiceUrl('processDefinition');
        $this->_client->setUri($url.$process_id);
        $this->_client->path = $url.$process_id;
        return $this->_getResponse();
    }

    public function getProcessDefinitionForm($process_id)
    {
        $url = $this->_getServiceUrl('processDefinition');
        $this->_client->setUri($url.$process_id.'/form?format=html');
        return $this->_getResponse();
    }

    public function createProcessInstance($process_id){
        $url = $this->_getServiceUrl('processInstance');
        $this->_client->setUri($url);
//        $this->_client->setRawData("{'processDefinitionId':'".$process_id."'}");
        $this->_client->postdata = json_encode( array('processDefinitionId'=>$process_id) );
        return $this->_getResponse('POST');
    }

    public function getProcessInstances(){
        $url = $this->_getServiceUrl('processInstances');
        $this->_client->setUri($url);
        return $this->_getResponse();
    }

    public function getProcessInstanceDiagram($instance_id){
        $url = sprintf($this->_getServiceUrl('processInstanceDiagram'),$instance_id);
        $this->_client->setUri($url);
//        return $this->_client->request()->getBody();
        return $this->_client->getContent();
    }

    public function getTasksSummary($user_id){
        $url = sprintf($this->_getServiceUrl('tasksSummary'),$user_id);
        $this->_client->setUri($url);
        return $this->_getResponse();
    }

    public function getTasksAssigned($user_id){
        $url = $this->_getServiceUrl('tasks');
        $this->_client->setUri($url.'assignee='.$user_id);
        return $this->_getResponse();
    }

    public function getTask($task_id){
        $url = sprintf($this->_getServiceUrl('task'),$task_id);
        $this->_client->setUri($url);
        return $this->_getResponse();
    }

    public function getTaskForm($task_id){
        $url = sprintf($this->_getServiceUrl('task'),$task_id.'/form');
        $this->_client->setUri($url);
//        return $this->_client->request()->getBody();
        return $this->_client->getContent();
    }

    private function _getServiceUrl($service_name)
    {
        if(isset($this->_services[$service_name])) {
            return self::BASE_URL.$this->_services[$service_name];
        }
        throw new InvalidArgumentException('Service not found');
    }

    private function _getResponse($method = 'GET'){
//        $body = $this->_client->request($method)->getBody();
        $this->_client->buildRequest();
        $body = $this->_client->getContent();
        $result = self::_toJson($body);
        return $result;
    }

    private static function _toJson($data){
        return json_decode($data,true);
    }

    //
    public function get($uid)
    {
        echo "TaskSummary:".var_dump( $this->getTasksSummary($uid) );
    }
} 