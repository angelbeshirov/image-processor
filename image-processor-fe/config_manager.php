<?php
    class config_manager {
        private $CONFIGS;

        public function __construct() {
            $this->CONFIGS = parse_ini_file("config/config.ini", false, INI_SCANNER_RAW);
        }

        public function get_key($key) {
            return $this->CONFIGS[$key];
        }
    }
?>
