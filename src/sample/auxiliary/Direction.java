package sample.auxiliary;

public enum Direction {
    UP {
        @Override
        public boolean up() {
            return true;
        }
    }, RIGHT {
        @Override
        public boolean right() {
            return true;
        }
    }, DOWN {
        @Override
        public boolean down() {
            return true;
        }
    }, LEFT {
        @Override
        public boolean left() {
            return true;
        }
    };

    public boolean up() {
        return false;
    }

    public boolean down() {
        return false;
    }

    public boolean left() {
        return false;
    }

    public boolean right() {
        return false;
    }
}
