alter table project_tasks add column statusId integer null references statuses(id);