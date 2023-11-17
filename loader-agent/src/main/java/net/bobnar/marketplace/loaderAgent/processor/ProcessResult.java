package net.bobnar.marketplace.loaderAgent.processor;

public abstract class ProcessResult {
    public String status;
    public String errors;

    public void fail() {
        this.status = "fail";
    }

    public void fail(String errors) {
        this.status = "fail";
        if (this.errors != null && !this.errors.isEmpty()) {
            this.errors += "\n" + errors;
        } else {
            this.errors = errors;
        }
    }

    public void success() {
        this.status = "ok";
    }

    public boolean isSuccess() {
        return "ok".equals(this.status);
    }

    public boolean isFailed() {
        return !this.isSuccess();
    }
}
