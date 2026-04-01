## Summary of Changes

### Backend

* Fixed SQL query logic in `TaskRepository` by properly grouping conditions using parentheses. This ensures `archived`, `status`, and search filters apply consistently.
* Replaced incorrect `Pageable` import (`java.awt.print.Pageable`) with `org.springframework.data.domain.Pageable`, enabling proper Spring Data pagination.
* Refactored pagination from in-memory slicing to database-level pagination using `Pageable` and `PageRequest`, improving performance and scalability.
* Added validation for `status` parameter in `TaskController` to prevent runtime crashes on invalid enum values.
* Removed artificial delay (`Thread.sleep`) which was blocking request threads and degrading performance.
* Added safeguards for invalid pagination inputs (e.g., page ≤ 0, pageSize ≤ 0).

### Frontend

* Fixed pagination bug where page was not reset when search query or status filter changed, causing empty or incorrect results.
* Introduced debounce logic for search input to reduce excessive API calls and improve performance.
* Replaced hardcoded page size with a constant (`PAGE_SIZE`) to improve maintainability and consistency.

## What I Did Not Change

* Did not refactor native SQL to JPQL/Criteria API to keep changes minimal within the timebox.
* Did not add backend or frontend tests due to time constraints.
* Did not implement caching or indexing optimizations.

## Biggest Remaining Risk

* Use of native SQL queries reduces maintainability and flexibility.
* Lack of input validation for extreme values (e.g., very large pageSize).
* No automated tests to catch regressions.

## Tools Used

* Used ChatGPT to validate SQL logic, debug pagination issues, and refine fixes.