package romanow.abc.bridge.constants;

public enum AnswerStatus {
    CHECKING, // Got an answer from student
    REVISION, // Teacher wrote a message in return
    REJECTED, // Teacher put 'bad' mark
    APPROVED, // Teacher put 'good' mark
    ;
}
