<?php

class SinchTokenGenerator {
    
    private $applicationKey;
    private $applicationSecret;
    
    public function __construct($applicationKey, $applicationSecret){
        $this->applicationKey = $applicationKey;
        $this->applicationSecret = $applicationSecret;
    }
    
    public function generateTicket($username, DateTime $createdAt, $expiresIn){
        
        $userTicket = [
            'identity' => [
                'type'      => 'username',
                'endpoint'  => $username,
            ],
                'expiresIn'         => $expiresIn,
                'applicationKey'    => $this->applicationKey,
                'created'           => $createdAt->format('c'),
        ];
            
        $userTicketJson = preg_replace('/\s+/', '', json_encode($userTicket));
        $userTicketBase64 = $this->base64Encode($userTicketJson);
        $digest = $this->createDigest($userTicketJson);
        $signature = $this->base64Encode($digest);
        $userTicketSigned = $userTicketBase64.':'.$signature;
        return $userTicketSigned;
        
    }
    
    private function base64Encode($data){
        return trim(base64_encode($data));
    }
    
    private function createDigest($data){
        return trim(hash_hmac('sha256', $data, base64_decode($this->applicationSecret), true));
    }
}

var userEmail = $_POST['username'];

$generator = new SinchTokenGenerator('da09f365-18e7-47d0-ad41-e3419bca7ac0','70qsOKjHXUiGykDsGZIDiw==');
$signUserTicket = $generator.generateTicket($userEmail, new DateTime(), 3600);

?>