package com.example.parking_system.batch;

import com.example.parking_system.entity.ParkingRecord;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class ParkingDataBatchConfig {

    @Bean
    public FlatFileItemReader<ParkingRecord> reader(@Value("${input.file}") String inputFile) {
        return new FlatFileItemReaderBuilder<ParkingRecord>()
                .name("parkingRecordReader")
                .resource(new FileSystemResource(inputFile))
                .delimited()
                .names("carNumber", "carType", "entryTime", "exitTime")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(ParkingRecord.class);
                }})
                .build();
    }

    @Bean
    public ParkingRecordItemProcessor processor() {
        return new ParkingRecordItemProcessor();
    }

    @Bean
    public JpaItemWriter<ParkingRecord> writer(EntityManagerFactory emf) {
        JpaItemWriter<ParkingRecord> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     FlatFileItemReader<ParkingRecord> reader,
                     ParkingRecordItemProcessor processor,
                     JpaItemWriter<ParkingRecord> writer) {

        return new StepBuilder("step", jobRepository)
                .<ParkingRecord, ParkingRecord>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importParkingJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("importParkingJob", jobRepository)
                .start(step)
                .build();
    }
}
