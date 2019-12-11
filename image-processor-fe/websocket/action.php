<?php

    abstract class Action {
        protected $type;

        public function get_type() {
            return $this->type;
        }
    }

	class Insert extends Action implements JsonSerializable {
        private $position;
        private $data;

        public function __construct($position, $data) {
            $this->type = Type::INSERT_TYPE;
            $this->position = $position;
            $this->data = $data;
        }

        public function get_position() {
            return $this->position;
        }

        public function get_data() {
            return $this->data;
        }

        public function jsonSerialize() {
            return [
                "type" => $this->type,
                "position" => $this->position,
                "data" => $this->data
            ];
        }
    }

    class UpdateUsers extends Action implements JsonSerializable {
        private $value;

        public function __construct($value) {
            $this->type = Type::UPDATE_USERS_TYPE;
            $this->value = $value;
        }

        public function get_value() {
            return $this->value;
        }

        public function jsonSerialize() {
            return [
                "type" => $this->type,
                "value" => $this->value
            ];
        }
    }

    class Delete extends Action implements JsonSerializable {
        private $from;
        private $to;

        public function __construct($from, $to) {
            $this->type = Type::DELETE_TYPE;
            $this->from = $from;
            $this->to = $to;
        }

        public function get_from() {
            return $this->from;
        }

        public function get_to() {
            return $this->to;
        }

        public function jsonSerialize() {
            return [
                "type" => $this->type,
                "from" => $this->from,
                "to" => $this->to
            ];
        }
    }

    class Init extends Action implements JsonSerializable {
        public function __construct() {
            $this->type = Type::INITIALIZE_TYPE;
        }

        public function jsonSerialize() {
            return [
                "type" => $this->type
            ];
        }
    }


    abstract class Type { 
        const INSERT_TYPE = "INSERT"; 
        const DELETE_TYPE = "DELETE"; 
        const UPDATE_USERS_TYPE  = "UPDATE USERS";
        const INITIALIZE_TYPE = "INITIALIZE";
    }
?>