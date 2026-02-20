package edo_public_api.kafka;


import common.dto.ApprovalDto;
import common.dto.EditRequestKafka.EditAdditionalApprovalRequest;
import common.dto.AdditionalApprovalDto;
import common.dto.EditRequestKafka.EditApprovalRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class EdoProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    public void addAppSave (AdditionalApprovalDto dto) {
        kafkaTemplate.send("additional-approval-save-request", dto);
    }

    public void addAppDelete (Long id) {
        kafkaTemplate.send("additional-approval-delete-request", id);
    }

    public AdditionalApprovalDto getById(Long id) throws Exception {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>("additional-approval-get-request", id);
        RequestReplyFuture<String, Object, Object> future =
                replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (AdditionalApprovalDto) response.value();
    }

    public AdditionalApprovalDto editAddApproval(Long id, AdditionalApprovalDto dto) throws Exception {

        EditAdditionalApprovalRequest request = new EditAdditionalApprovalRequest(id, dto);
        ProducerRecord<String, Object> record =
                new ProducerRecord<>("additional-approval-edit-request", request);
        RequestReplyFuture<String, Object, Object> future =
                replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (AdditionalApprovalDto) response.value();
    }

    /*--------------------------------------------------------*/
    public List<ApprovalDto> getAllApprovals() throws Exception {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>("approval-get-all-request", null);
        RequestReplyFuture<String, Object, Object> future =
                replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (List<ApprovalDto>) response.value();
    }

    public ApprovalDto appSave (ApprovalDto dto) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>("approval-save-request", dto);
        RequestReplyFuture<String, Object, Object> future =
                replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (ApprovalDto) response.value();
    }

    public void appDelete (Long id) {
        kafkaTemplate.send("approval-delete-request", id);
    }

    public ApprovalDto appGetById(Long id) throws Exception {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>("approval-get-request", id);
        RequestReplyFuture<String, Object, Object> future =
                replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (ApprovalDto) response.value();
    }

    public ApprovalDto approvalEdit(Long id, ApprovalDto dto) throws Exception {

        EditApprovalRequest request = new EditApprovalRequest(id, dto);
        ProducerRecord<String, Object> record =
                new ProducerRecord<>("approval-edit-request", request);
        RequestReplyFuture<String, Object, Object> future =
                replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (ApprovalDto) response.value();
    }

}
