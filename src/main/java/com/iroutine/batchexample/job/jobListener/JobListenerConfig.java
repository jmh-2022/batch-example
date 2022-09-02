package com.iroutine.batchexample.job.jobListener;

import com.iroutine.batchexample.job.validateParam.validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/*
* desc: 파일 이름 파라미터 전달 그리고 검증
* run: --spring.batch.job.names=validateParamJob -fileName=test.csv
*
* */
//@Configuration
@RequiredArgsConstructor
public class JobListenerConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validateParamJob(Step jobListenerStep){
        return jobBuilderFactory.get("jobListener")
                .incrementer(new RunIdIncrementer())
//                .validator(multipleValidator())
                .listener(new JobLoggerListener())
                .start(jobListenerStep)
                .build();

    }

    private CompositeJobParametersValidator multipleValidator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

        validator.setValidators(Arrays.asList(new FileParamValidator()));

//        validator.setValidators(Arrays.asList(new FileParamValidator())), , , ;

        return validator;
    }

    @JobScope
    @Bean
    public Step jobListenerStep(Tasklet jobListenerTasklet) {
        return stepBuilderFactory.get("jobListenerStep")
                .tasklet(jobListenerTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet jobListenerTasklet(@Value("#{jobParameters['fileName']}") String fileName){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                System.out.println("Hello World Spring Batch");
//                return RepeatStatus.FINISHED;
                throw new Exception("Failed!!!!!!!!!");
            }
        };
    }
}
