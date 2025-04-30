import java.util.ArrayList;
import java.util.List;

public class CBMCResult {
    private List<String> successful = new ArrayList<String>();
    private List<String> failure = new ArrayList<String>();
    private List<String> other = new ArrayList<String>();
    private List<String> timeout = new ArrayList<String>();

    public CBMCResult(List<String> successful, List<String> failure, List<String> other, List<String> timeout) {
        this.successful = successful;
        this.failure = failure;
        this.other = other;
        this.timeout = timeout;
    }

    public List<String> getSuccessful() {
        return successful;
    }

    public void setSuccessful(List<String> successful) {
        this.successful = successful;
    }

    public List<String> getFailure() {
        return failure;
    }

    public void setFailure(List<String> failure) {
        this.failure = failure;
    }

    public List<String> getOther() {
        return other;
    }

    public void setOther(List<String> other) {
        this.other = other;
    }

    public List<String> getTimeout() {
        return timeout;
    }

    public void setTimeout(List<String> timeout) {
        this.timeout = timeout;
    }
}
