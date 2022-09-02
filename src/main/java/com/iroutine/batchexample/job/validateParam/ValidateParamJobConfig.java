package com.iroutine.batchexample.job.validateParam;

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
public class ValidateParamJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validateParamJob(Step validateStep){
        return jobBuilderFactory.get("validateParamJob")
                .incrementer(new RunIdIncrementer())
                .validator(multipleValidator())
                .start(validateStep)
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
    public Step validateParamStep(Tasklet validateParamTasklet) {
        return stepBuilderFactory.get("validateParamStep")
                .tasklet(validateParamTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet validateParamTasklet(@Value("#{jobParameters['fileName']}") String fileName){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Hello World Spring Batch");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
