package com.innowise.candidatemodule.service.impl;

import com.innowise.candidatemodule.domain.Attachment;
import com.innowise.candidatemodule.domain.Candidate;
import com.innowise.candidatemodule.domain.Department;
import com.innowise.candidatemodule.domain.Employee;
import com.innowise.candidatemodule.domain.Interview;
import com.innowise.candidatemodule.domain.InterviewEmployee;
import com.innowise.candidatemodule.repository.AttachmentRepository;
import com.innowise.candidatemodule.repository.CandidateRepository;
import com.innowise.candidatemodule.repository.DepartmentRepository;
import com.innowise.candidatemodule.repository.EmployeeRepository;
import com.innowise.candidatemodule.repository.InterviewEmployeeRepository;
import com.innowise.candidatemodule.repository.InterviewRepository;
import com.innowise.candidatemodule.service.api.CandidateService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    private CandidateRepository candidateRepository;

    private AttachmentRepository attachmentRepository;

    private InterviewRepository interviewRepository;

    private DepartmentRepository departmentRepository;

    private EmployeeRepository employeeRepository;

    private InterviewEmployeeRepository interviewEmployeeRepository;

    @Autowired
    public CandidateServiceImpl(final CandidateRepository candidateRepository,
                                final AttachmentRepository attachmentRepository,
                                final InterviewRepository interviewRepository,
                                final DepartmentRepository departmentRepository,
                                final EmployeeRepository employeeRepository,
                                final InterviewEmployeeRepository interviewEmployeeRepository) {
        this.candidateRepository = candidateRepository;
        this.attachmentRepository = attachmentRepository;
        this.interviewRepository = interviewRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.interviewEmployeeRepository = interviewEmployeeRepository;
    }


    @Override
    @Transactional
    public Candidate save(final Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public void reset(@NonNull final Candidate candidate) {
        candidateRepository.delete(candidate);
    }

    @Override
    @Transactional
    public Candidate update(final Candidate candidate) {
        Candidate result = candidateRepository.getOne(candidate.getId());
        if (candidateRepository.existsById(candidate.getId())) {
            result = candidateRepository.getOne(candidate.getId());
        }
        result.setFirstName(candidate.getFirstName());
        result.setLastName(candidate.getLastName());
        result.setMail(candidate.getMail());
        result.setTelephone(candidate.getTelephone());
        result.setSkype(candidate.getSkype());
        result.setDescription(candidate.getDescription());
        updateAttachments(result, attachmentRepository.findAllByCandidateId(candidate.getId()));
        updateInterviews(result, interviewRepository.findAllByCandidateId(candidate.getId()));
        return candidateRepository.save(result);
    }

    @Override
    @Transactional
    public Optional<Candidate> getById(final Long id) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        return Optional.ofNullable(candidate);
    }

    @Override
    @Transactional
    public List<Candidate> getAll(){
        return candidateRepository.findAll();
    }

    @Override
    @Transactional
    public List<Candidate> getPage(int page, int size, String sortColumn, String sortDirection) {
        PageRequest pageRequest;
        if (sortDirection.equals("NULL")) {
            pageRequest = PageRequest.of(page, size);
        } else {
            pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortDirection), sortColumn);
        }
        return candidateRepository.findAll(pageRequest).getContent();
    }

    public void fillDatabase() {
        Candidate candidate1 = Candidate.builder().firstName("Aleksei").lastName("Petrou").mail("abc@mail.ru").skype("1111").description("aaa").telephone("3752912345550").build();
        Candidate candidate2 = Candidate.builder().firstName("Petr").lastName("Alekseeu").mail("abb@mail.ru").skype("1234").description("aba").telephone("3752912345555").build();
        Candidate candidate3 = Candidate.builder().firstName("Petr").lastName("Ivanou").mail("acb@mail.ru").skype("1233").description("ccc").telephone("375291236655").build();
        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);

        Attachment attachment1 = Attachment.builder().fileName("abc").path("home/aaa/abc").candidate(candidate2).build();
        Attachment attachment2 = Attachment.builder().fileName("abbbb").path("home/aaa/abbbb").candidate(candidate1).build();
        attachmentRepository.save(attachment1);
        attachmentRepository.save(attachment2);

        Department department1 = Department.builder().departmentName("Java").build();
        Department department2 = Department.builder().departmentName(".NET").build();
        Department department3 = Department.builder().departmentName("JS").build();
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        departmentRepository.save(department3);

        Employee employee1 = Employee.builder().firstName("Aleksandr").lastName("Petrou").department(department1).build();
        Employee employee2 = Employee.builder().firstName("Andrei").lastName("Petrou").department(department2).build();
        Employee employee3 = Employee.builder().firstName("Andrei").lastName("Pavlov").department(department2).build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        Interview interview1 = Interview.builder().candidate(candidate2).interviewDate(LocalDateTime.of(2019,4,15,15,0,0)).status("aaa").build();
        Interview interview2 = Interview.builder().candidate(candidate2).interviewDate(LocalDateTime.of(2019,6,20,15,0,0)).status("aaa").build();
        Interview interview3 = Interview.builder().candidate(candidate3).interviewDate(LocalDateTime.of(2019,4,25,15,0,0)).status("aaa").build();
        interviewRepository.save(interview1);
        interviewRepository.save(interview2);
        interviewRepository.save(interview3);

        InterviewEmployee interviewEmployee1 = InterviewEmployee.builder().interview(interview1).employee(employee2).feedback("ccc").build();
        InterviewEmployee interviewEmployee2 = InterviewEmployee.builder().interview(interview2).employee(employee2).feedback("bbb").build();
        InterviewEmployee interviewEmployee3 = InterviewEmployee.builder().interview(interview3).employee(employee1).feedback("aaa").build();
        interviewEmployeeRepository.save(interviewEmployee1);
        interviewEmployeeRepository.save(interviewEmployee2);
        interviewEmployeeRepository.save(interviewEmployee3);
    }

    private void updateAttachments(final Candidate candidate, final List<Attachment> attachments) {
        for(Attachment attachment : attachments) {
            attachment.setCandidate(candidate);
        }
        attachmentRepository.saveAll(attachments);
    }

    private void updateInterviews(final Candidate candidate, final List<Interview> interviews) {
        for(Interview interview : interviews) {
            interview.setCandidate(candidate);
        }
        interviewRepository.saveAll(interviews);
    }
}
